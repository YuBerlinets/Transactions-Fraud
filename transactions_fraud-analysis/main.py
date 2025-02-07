import json

from fastapi import FastAPI

from ml_model.test_model import evaluate_model
from models.models import Transaction
from service.fraud_detection import check_fraud_rules
from ml_model.ml_model import load_model, predict_fraud_ml

app = FastAPI()
model = load_model()


@app.post("/api/predict")
def predict_fraud(transaction: Transaction):
    print(transaction)
    is_fraud = predict_fraud_ml(model, transaction)
    response = {"transactionId": transaction.transactionId, "fraud": is_fraud, "reason": "Model prediction"}
    print(response)
    return response


@app.get("/evaluate")
def evaluate_mlmodel():
    with open("test_transactions.json", "r") as f:
        test_data = json.load(f)

    true_labels = [tx["trueLabel"] for tx in test_data]

    metrics = evaluate_model(model, test_data, true_labels)

    return {
        "message": "Evaluation completed",
        "results": metrics
    }
