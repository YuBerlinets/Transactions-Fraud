from pydantic import BaseModel
from typing import List


class PreviousTransaction(BaseModel):
    userId: str
    amount: float
    timestamp: str
    merchant: str
    location: str
    type: str


class Transaction(BaseModel):
    transactionId: str
    userId: str
    amount: float
    timestamp: str
    merchant: str
    location: str
    type: str
    previousTransactions: List[PreviousTransaction] = []
