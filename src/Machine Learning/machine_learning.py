import time

import requests
import pandas as pd

from sklearn.metrics import accuracy_score, classification_report
from sklearn.model_selection import train_test_split
from sklearn.tree import DecisionTreeClassifier
from sklearn.utils import resample


def get_patient_age(cholesterol_resource, patient_resource):
    date_checked = cholesterol_resource["effectiveDateTime"]
    year_checked = date_checked[:4]

    date_of_birth = patient_resource["birthDate"]
    year_of_birth = date_of_birth[:4]

    return int(year_checked) - int(year_of_birth)


def get_patient_gender(patient_resource):
    # convert gender to numeric form
    gender = patient_resource["gender"]

    if gender == "male":
        return -1
    elif gender == "female":
        return 1
    else:
        return 0


def get_patient_cholesterol_class(cholesterol_resource):
    MAX_CHOL_LEVEL = 212.6575  # limit for high cholesterol
    chol_level = cholesterol_resource["valueQuantity"]["value"]
    if chol_level > MAX_CHOL_LEVEL:
        return "High"
    else:
        return "Low"


def get_num_chol_visits(patient_resource, cholesterol_resource):
    # works because it's sorted
    patient_id = patient_resource["id"]
    BASE_URL = "https://fhir.monash.edu/hapi-fhir-jpaserver/fhir/"

    # get all observations with the patient id
    next_url = BASE_URL + "Observation?code=2093-3&_sort=date&patient=" + patient_id
    has_next = True

    # only get number of observations up to the current observation,
    # e.g. if this is the 3rd cholesterol observation we return 3
    num_chol = 0

    while has_next:
        has_next = False
        patient_chol_list = requests.get(next_url).json()
        current_chol_date = cholesterol_resource["effectiveDateTime"]
        has_next = True

        for chol_data in patient_chol_list["entry"]:
            num_chol += 1
            chol_date = chol_data["resource"]["effectiveDateTime"]

            if current_chol_date == chol_date:
                return num_chol  # works because cholesterol has to be in the list

        for link in patient_chol_list["link"]:
            if link["relation"] == "next":
                has_next = True
                next_url = link["url"]


def fetch_prediction():
    # dataset
    titles = ["age", "gender", "number of visits", "cholesterol class"]
    data_set = pd.DataFrame(columns=titles)

    # Filling the dataset
    MIN_ACCURACY = 0.80
    reached_accuracy = False
    total_rows = 0

    while not reached_accuracy:
        iter_per_round = 5  # add 500 instances to dataset per iteration
        iteration = 0

        # Request server for data
        BASE_URL = "https://fhir.monash.edu/hapi-fhir-jpaserver/fhir/"
        next_url = BASE_URL + "Observation?code=2093-3&_count=100"
        has_next = True

        while has_next and (iteration < iter_per_round):
            print(iteration)
            has_next = False

            req = requests.get(next_url)
            res = req.json()
            entries = res["entry"]

            for i in range(len(entries)):
                record = []
                observation = entries[i]

                # get the cholesterol and patient resources
                cholesterol_resource = observation["resource"]
                patient_reference = cholesterol_resource["subject"]["reference"]
                patient_resource = requests.get(BASE_URL + patient_reference).json()

                # parameters to include
                patient_age = get_patient_age(cholesterol_resource, patient_resource)
                patient_gender = get_patient_gender(patient_resource)
                num_visits = get_num_chol_visits(patient_resource, cholesterol_resource)
                chol_class = get_patient_cholesterol_class(cholesterol_resource)

                record.append(patient_age)
                record.append(patient_gender)
                record.append(num_visits)
                record.append(chol_class)

                data_set.loc[total_rows] = record
                total_rows += 1

            for i in range(len(res["link"])):
                if res["link"][i]["relation"] == "next":
                    has_next = True
                    next_url = res["link"][i]["url"]

            # increment iteration
            # if iteration is more than number fetched per round, exit and predict
            # this is to limit number of data set fetched; fetch as needed
            iteration += 1
            time.sleep(2)  # reduce load on server

        accuracy = predict(data_set)
        # if accuracy is sufficient, end program
        if accuracy >= MIN_ACCURACY:
            reached_accuracy = True


def predict(data_set):
    # Use upsampling to increase cholesterol data set
    # This is to prevent imbalanced data sets
    data_set_low = data_set[data_set["cholesterol class"] == "Low"]
    data_set_high = data_set[data_set["cholesterol class"] == "High"]

    num_data_sets_low = len(data_set_low)
    num_data_sets_high = len(data_set_high)

    print(data_set)
    print(data_set["cholesterol class"].value_counts())

    # upsample if data set is less
    # to achieve equal sample size for both cases
    if num_data_sets_low > num_data_sets_high:
        data_set_high_upsampled = resample(data_set_high, replace=True,
                                           n_samples=num_data_sets_low, random_state=123)
        data_set_upsampled = pd.concat([data_set_low, data_set_high_upsampled])
    elif num_data_sets_low < num_data_sets_high:
        data_set_low_upsampled = resample(data_set_low, replace=True,
                                          n_samples=num_data_sets_high, random_state=123)
        data_set_upsampled = pd.concat([data_set_low_upsampled, data_set_high])
    else:
        data_set_upsampled = pd.concat([data_set_low, data_set_high])

    print(data_set_upsampled["cholesterol class"].value_counts())

    # Splitting validation dataset
    array = data_set_upsampled.values
    x = array[:, 0:3]
    y = array[:, 3]

    # test size = 20%; training size = 80%
    X_train, X_validation, Y_train, Y_validation = train_test_split(x, y, test_size=0.20, random_state=1)

    # Predict
    model = DecisionTreeClassifier()
    model.fit(X_train, Y_train)
    prediction = model.predict(X_validation)

    # Print prediction scores
    print(accuracy_score(Y_validation, prediction))
    print(classification_report(Y_validation, prediction))

    return accuracy_score(Y_validation, prediction)


def main():
    fetch_prediction()


main()
