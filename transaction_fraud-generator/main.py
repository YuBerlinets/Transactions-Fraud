import os
import time
import requests
from dotenv import load_dotenv

from generator import generate_dataset

load_dotenv()


def send_transactions():
    url = os.getenv("BASE_URL") + "/api/transactions/send"
    while True:
        transactions = generate_dataset(20)
        for transaction in transactions:
            response = requests.post(url, json=transaction)
            print(response.status_code, response.text)

        print("Waiting for the next batch...")
        time.sleep(60)


if __name__ == "__main__":
    send_transactions()
