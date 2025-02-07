import json
import random
from datetime import datetime, timedelta
from typing import List

from sklearn.metrics import accuracy_score, precision_score, recall_score, f1_score

from service.fraud_detection import check_fraud_rules

LOCATIONS = [
    ("New York", "USA"), ("Los Angeles", "USA"), ("Chicago", "USA"), ("Miami", "USA"),
    ("Toronto", "Canada"), ("Vancouver", "Canada"), ("London", "UK"), ("Paris", "France"),
    ("Berlin", "Germany"), ("Madrid", "Spain"), ("Tokyo", "Japan"), ("Seoul", "South Korea")
]
MERCHANTS = ["Amazon", "Walmart", "BestBuy", "Apple Store", "Nike", "Adidas", "McDonald's", "Starbucks"]


def generate_previous_transactions(user_id):
    previous_transactions = []
    for _ in range(3):
        transaction = {
            "userId": user_id,
            "amount": random.uniform(10, 1400),
            "timestamp": (datetime.now() - timedelta(days=random.randint(1, 30))),
            "merchant": random.choice(MERCHANTS),
            "location": random.choice(LOCATIONS)[0] + "," + random.choice(LOCATIONS)[1],
            "type": "offline" if random.random() > 0.3 else "online"
        }
        previous_transactions.append(transaction)
    return previous_transactions


def generate_transaction(user_id, previous_transactions):
    base_amount = random.uniform(10, 1400)

    transaction = {
        "transactionId": str(random.randint(10000, 99999)),
        "userId": user_id,
        "amount": base_amount,
        "timestamp": (previous_transactions[-1]["timestamp"] + timedelta(
            minutes=random.randint(1, 10))) if previous_transactions else datetime.now(),
        "merchant": random.choice(MERCHANTS),
        "location": random.choice(LOCATIONS)[0] + "," + random.choice(LOCATIONS)[1],
        "type": "offline" if random.random() > 0.3 else "online",
        "previousTransactions": list(previous_transactions)
    }

    # print(f"Generated transaction: {transaction}")
    if check_fraud_rules(transaction):
        transaction["trueLabel"] = 1
    else:
        transaction["trueLabel"] = 0

    transaction["timestamp"] = transaction["timestamp"].isoformat() + "Z"
    for tx in transaction["previousTransactions"]:
        tx["timestamp"] = tx["timestamp"].isoformat() + "Z"
    return transaction


def generate_dataset(num_users=1000):
    dataset = []
    for user_id in range(1, num_users + 1):
        user_id = f"user{user_id}"
        previous_transactions = generate_previous_transactions(user_id)

        transaction = generate_transaction(user_id, previous_transactions)

        if len(previous_transactions) > 3:
            previous_transactions.pop(0)
        dataset.append(transaction)
    return dataset


def save_dataset():
    dataset = generate_dataset()
    with open("backend_test.json", "w") as f:
        json.dump(dataset, f, indent=4, default=str)

    print("Dataset with true labels saved to test_transactions.json")


def evaluate_model(model, test_data: List[dict], true_labels: List[int]):
    predictions = []
    for tx in test_data:
        time_hour = datetime.fromisoformat(tx["timestamp"]).hour
        features = [
            tx["amount"],
            time_hour,
            time_hour < 6 or time_hour > 22,
            tx["type"].lower() == "online"
        ]

        prediction = model.predict([features])[0]
        predictions.append(prediction)

    accuracy = accuracy_score(true_labels, predictions)
    precision = precision_score(true_labels, predictions)
    recall = recall_score(true_labels, predictions)
    f1 = f1_score(true_labels, predictions)

    print(f"Accuracy: {accuracy:.4f}")
    print(f"Precision: {precision:.4f}")
    print(f"Recall: {recall:.4f}")
    print(f"F1-score: {f1:.4f}")

    return {"accuracy": accuracy, "precision": precision, "recall": recall, "f1": f1}
