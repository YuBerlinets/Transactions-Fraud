import pickle
import pandas as pd
from sklearn.tree import DecisionTreeClassifier
from datetime import datetime

MODEL_FILE = "fraud_model.pkl"


def train_model(train_data_path):
    df = pd.read_csv(train_data_path)

    df["timestamp"] = pd.to_datetime(df["timestamp"])
    df["hour"] = df["timestamp"].dt.hour
    df["is_night"] = df["hour"].apply(lambda x: 1 if x < 6 or x > 22 else 0)
    df["is_online"] = df["type"].apply(lambda x: 1 if x.lower() == "online" else 0)

    features = ["amount", "hour", "is_night", "is_online"]
    X = df[features]
    y = df["is_fraud"]

    model = DecisionTreeClassifier()
    model.fit(X, y)


    with open(MODEL_FILE, "wb") as f:
        pickle.dump(model, f)
    print("Model trained and saved successfully.")


def load_model():
    with open("./ml_model/fraud_model.pkl", "rb") as f:
        return pickle.load(f)


def predict_fraud_ml(model, transaction):
    timestamp = datetime.fromisoformat(transaction.timestamp)
    is_night = 1 if timestamp.hour < 6 or timestamp.hour > 22 else 0
    is_online = 1 if transaction.type.lower() == "online" else 0

    X_new = [[transaction.amount, timestamp.hour, is_night, is_online]]
    prediction = model.predict(X_new)
    return bool(prediction[0])
