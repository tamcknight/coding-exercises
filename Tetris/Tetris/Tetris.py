import sys

board = None
blocks = {
    "T": [[1, 1, 1],
          [0, 1, 0]],

    "S": [[0, 2, 2],
          [2, 2, 0]],

    "Z": [[3, 3, 0],
          [0, 3, 3]],

    "L": [[4, 0],
          [4, 0],
          [4, 4]],

    "J": [[0, 5],
          [0, 5],
          [5, 5]],

    "I": [[6, 6, 6, 6]],

    "Q": [[7, 7],
          [7, 7]],
}


def check_for_collision(shape, x_coord, y_coord):
    global board
    #check to see if the game piece collides with the space
    #where it is being placed on the Y axis
    for enum_y, row in enumerate(shape):
        for enum_x, cell in enumerate(row):
            try:
                if cell and board[enum_y + y_coord][enum_x + x_coord]:
                    return True
            except IndexError:
                return True
    return False


def find_y_coord(shape, x_coord):
    y_coord = None

    #work from top to bottom on the board until a collision is detected.
    #once one is detected the last valid Y coord is where it should be put
    for row in range(len(board)):
        if check_for_collision(shape, x_coord, row):
            break
        else:
            y_coord = row

    #everyone needs to be pal'd once in a while
    if y_coord is None:
        raise Exception("You're out of bounds pal.")

    return y_coord


def find_full_rows():
    #using the global keyowrkd tells python to use the global board variable instead of a local one
    global board
    #this is reversed because if you delete off of the front of the board first
    #the offset is not correct, but if you start at the back and work down
    #the rows further down the array are unaffected
    for row in reversed(range(len(board))):
        if row_is_full(board[row]):
            del board[row]


def row_is_full(row):
    #this is pretty straight forward.  go through each row and see if all the
    #values are greater than 0
    row_is_full = True
    for value in row:
        if value == 0:
            row_is_full = False
            break
    return row_is_full


def place_block(board, block, base_coordinates):
    #at this point where know where the block needs to go.
    #drop it in!
    x_coord, y_coord = base_coordinates
    for cy, row in enumerate(block):
        for cx, val in enumerate(row):
            board[cy + y_coord ][cx + x_coord] += val

def drop(block, x_coord):
    global board

    #the Y Coord needs to be found so it is known where the block should go.
    #the X coord is already known
    y_coord = find_y_coord(block, x_coord)
    #put the block in it's home
    place_block(board, block, (x_coord, y_coord))
    #find any rows that have a value > 0 for all elements and remove it.
    find_full_rows()


def initialize_board():
    ##initialize the board with all 0's
    # the board is a multidimensional array
    global board
    width = 10
    height = 16

    board = [None] * height
    for y in range(height):
        board[y] = [0] * width


def read_in_moves_file(file_name):
    ##pretty simple.. read in the input file
    move_list = []
    with open(file_name, 'r') as f:
        lines = f.readlines()
        for line in lines:
            move_list.append(line.rstrip().split(","))
        f.close()
    return move_list


def print_board():
    #dump the board to STDOUT
    #this is usefule for debugging
    global board
    for row in (board):
        print(row)
    print ("\n")


def find_max_height():
    global board
    height = 0
    #start at the bottom of the board and count up
    for row in reversed(board):
        if row_has_value(row):
            height += 1
    return height


def row_has_value(row):
    # find out if there is a block in the row that has a value >0.
    #this means that a block was there
    has_value = False
    for value in row:
        if value > 0:
            has_value = True
    return has_value


def play_game(move_line):
    global blocks
    ##create a fresh game board re-init from the last game played
    initialize_board()

    # iterate thru the list of data for this game and start dropping
    for block_and_coord in move_line:
        block_string = block_and_coord[0]
        x_coord = int(block_and_coord[1])
        block = blocks[block_string]

        ##now we have the block to drop and where to drop it in
        drop(block, x_coord)

    print(find_max_height())


def main():
    file_name = sys.argv[1]
    move_list = read_in_moves_file(file_name)
    for move_line in move_list:
        play_game(move_line)


if __name__ == "__main__":
    main()
