import os
import time
import requests
from dotenv import load_dotenv

from generator import generate_dataset

load_dotenv()


def send_transactions():
    url = os.getenv("BASE_URL") + "/api/transactions/send"

    while True:
        try:
            # Check if the server is available before sending transactions
            response = requests.get(url, timeout=5)
            if response.status_code != 200:
                print(f"Server unavailable, skipping this batch. Status code: {response.status_code}")
                time.sleep(60)
                continue
        except requests.exceptions.RequestException as e:
            print(f"Error reaching the server: {e}, skipping this batch...")
            time.sleep(60)
            continue

        transactions = generate_dataset(100)

        for transaction in transactions:
            try:
                response = requests.post(url, json=transaction, timeout=5)
                print(response.status_code, response.text)
            except requests.exceptions.RequestException as e:
                print(f"Failed to send transaction: {e}")

        print("Waiting for the next batch...")
        time.sleep(60)


if __name__ == "__main__":
    send_transactions()
