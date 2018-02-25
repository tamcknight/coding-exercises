import sys
from random import *

def generate_coins(num_coins):

    #initialize the list with a 1 so you can always solve the problem
    coins = [1]
    while len(coins) < int(num_coins):
        #assume no coin has a value greater than 100
        coins.append(randint(1,100))
    return coins

def num_coins_for_amount(coin_value, amount):
    value = amount//coin_value
    return value

def main():
    target_amount = input("Enter a Target Amount: ")
    num_coins = input("Enter the number of random coins to use: ")

    coins = generate_coins(num_coins)
    # reverse the sort order so we start largest coin value
    coins.sort(reverse=True)
    #use a dictionary to store coinns and the count of the coins we will use
    coins_used = {}
    target_amount_residual = int(target_amount)

    for coin in coins:
        if target_amount_residual != 0:
            num_coins = num_coins_for_amount(coin, target_amount_residual)
            if num_coins > 0:
                coins_used[coin] = num_coins
                target_amount_residual -= coin * num_coins

    print("Target Amount: " + target_amount)
    print("Number of coins used : " + str(num_coins))
    print("Coin values:" + str(coins))
    print("Minimum number of coins used " + str(coins_used))

if __name__ == "__main__":
    main()