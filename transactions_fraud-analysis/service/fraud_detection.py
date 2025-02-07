from datetime import datetime


# def check_fraud_rules(transaction):
#     if len(transaction.previousTransactions) < 2:
#         return False
#
#     last_two_locs = {t.location for t in transaction.previousTransactions[-2:]}
#     last_three_timestamps = [datetime.fromisoformat(t.timestamp) for t in transaction.previousTransactions]
#
#     if len(last_two_locs) > 1 and transaction.type.lower() != "online":
#         return True
#
#     if len(last_three_timestamps) >= 3:
#         time_diffs = [(last_three_timestamps[i] - last_three_timestamps[i - 1]).total_seconds() for i in range(1, 3)]
#         if all(diff < 300 for diff in time_diffs):  # 300 seconds = 5 minutes
#             return True
#
#     last_three_sum = sum(t.amount for t in transaction.previousTransactions[-3:])
#     hour = datetime.fromisoformat(transaction.timestamp).hour
#     is_night = 1 if hour < 6 or hour > 22 else 0
#
#     if transaction.amount >= 2 * last_three_sum and is_night:
#         return True
#
#     return False

def check_fraud_rules(transaction):
    if len(transaction['previousTransactions']) < 2:
        return False

    last_two_locs = {t['location'] for t in transaction['previousTransactions'][-2:]}
    last_three_timestamps = [t['timestamp'] for t in transaction['previousTransactions']]

    # Rule 1: If last two transactions were in different locations and type is not "online", flag fraud
    if len(last_two_locs) > 1 and transaction['type'].lower() != "online":
        return True

    # Rule 2: If last three transactions happened within 5 minutes, flag fraud
    if len(last_three_timestamps) >= 3:
        time_diffs = [(last_three_timestamps[i] - last_three_timestamps[i - 1]).total_seconds() for i in range(1, 3)]
        if all(diff < 300 for diff in time_diffs):
            return True

    # Rule 3: If amount is 2x sum of last three transactions and it's night, flag fraud
    last_three_sum = sum(t['amount'] for t in transaction['previousTransactions'][-3:])
    hour = transaction['timestamp'].hour
    is_night = 1 if hour < 6 or hour > 22 else 0

    if transaction['amount'] >= 2 * last_three_sum and is_night:
        return True

    return False

