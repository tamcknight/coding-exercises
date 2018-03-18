import tetris
import sys

def test_game():
    #
    answers_from_run = []
    answers = []
    file_name = "../data/input.txt"
    move_list = tetris.read_in_moves_file(file_name)
    for move_line in move_list:
        answers_from_run.append(tetris.play_game(move_line))

    answers = read_in_answers()

    result = map(lambda x, y: x == y, answers_from_run, answers)
    if not all(map(lambda x: x == True, result)):
        raise Exception("Game test has failed.", result)
    else:
        print("All game tests passed.")


def read_in_answers():
    answers = []
    try:
        with open("../data/answers.txt", 'r') as f:
            lines = f.readlines()
            for line in lines:
                answers.append(int(line.rstrip()))
            f.close()
    except Exception as e:
        print("Error reading file "+e)
        sys.exit()
    return answers

def main():
    test_game()


if __name__ == "__main__":
    main()