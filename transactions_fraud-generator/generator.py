import json
import random
from datetime import datetime, timedelta
from typing import List

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
            "timestamp": (datetime.now() - timedelta(minutes=random.randint(1, 60))),
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


    transaction["timestamp"] = transaction["timestamp"].isoformat() + "Z"
    for tx in transaction["previousTransactions"]:
        tx["timestamp"] = tx["timestamp"].isoformat() + "Z"
    return transaction


def generate_dataset(num_users=10):
    dataset = []
    for user_id in range(1, num_users + 1):
        user_id = f"user{random.randint(1000, 1050)}"
        previous_transactions = generate_previous_transactions(user_id)

        transaction = generate_transaction(user_id, previous_transactions)

        if len(previous_transactions) > 3:
            previous_transactions.pop(0)
        dataset.append(transaction)
    return dataset

