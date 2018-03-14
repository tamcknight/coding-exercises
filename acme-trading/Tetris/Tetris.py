import sys


board = None
blocks = {
    "T": [[0, 1, 0],
          [1, 1, 1]],

    "S": [[2, 2, 0 ],
          [0, 2, 2]],

    "Z": [[0, 3, 3],
          [3, 3, 0]],

    "L": [[4, 4],
          [4, 0],
          [4, 0]],

    "J": [[5, 5],
          [0, 5],
          [0, 5]],

    "I": [[6, 6, 6, 6]],

    "Q": [[7, 7],
          [7, 7]],
}


def check_for_collision( shape, x_coord, y_coord):
    for cy, row in enumerate(shape):
        for cx, cell in enumerate(row):
            try:
                if cell and board[cy + y_coord][cx + x_coord]:
                    return True
            except IndexError:
                return True
    return False

def find_y_coord( shape, x_coord):
    #start at the bottom and work up
    y_coord = 0

    for row in range(len(board)):
        if not check_for_collision(shape, x_coord, row):
            y_coord = row
            break
    return y_coord


def find_full_rows():
    global board
    for row in reversed(range(len(board))):
        if row_is_full(board[row]):
            del board[row]

def row_is_full(row):

    row_is_full = True
    for value in row:
        if value == 0:
            row_is_full = False
            break
    return row_is_full


def join_matrixes(board, block, base_coordinates):
    off_x, off_y = base_coordinates
    for cy, row in enumerate(block):
        for cx, val in enumerate(row):
            board[cy + off_y ][cx + off_x] += val
    return board


def drop( block, x_coord):
    global board
    y_coord = find_y_coord(block,x_coord)
    board = join_matrixes(board,block, (x_coord, y_coord))
    find_full_rows()
    print_board()



def initialize_board():
    global board
    width = 10
    height = 16

    board = [None] * height
    for y in range(height):
        board[y] = [0] * width


def read_in_moves_file(file_name):
    move_list = []
    with open(file_name, 'r') as f:
        lines = f.readlines()
        for line in lines:
            move_list.append(line.rstrip().split(","))
        f.close()
    return move_list

def print_board():
    global board
    for row in reversed(board):
        print(row)
    print ("\n")

def find_max_height():
    global board
    height = 0

    for row in board:
        if row_has_value(row):
            height += 1
    return height

def row_has_value(row):
    has_value = False
    for value in row:
        if value > 0:
            has_value = True
    return has_value



def play_game(move_line):
    global blocks
    ##create a fresh game tank so remove the old games
    initialize_board()

    # iterate thru the list of data for this game and start dropping
    for block_and_coord in move_line:
        block_string = block_and_coord[0]
        x_coord = int(block_and_coord[1])
        block = blocks[block_string]

        ##now we have the block to drop and where to drop it in
        drop( block, x_coord)

    print(find_max_height())


def main():
    file_name = sys.argv[1]
    move_list = read_in_moves_file(file_name)
    for move_line in move_list:
        play_game(move_line)


if __name__ == "__main__":
    main()
