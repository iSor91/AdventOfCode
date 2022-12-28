from constants import *
def get_max(tiles, iscolumn):
    return max(tiles.keys(), key=lambda t: t[1 if iscolumn else 0])

def get_max_columns(tiles):
    return get_max(tiles, True)

def get_max_rows(tiles):
    return get_max(tiles, False)

def get_tiles(tiles, index, iscolumn):
    return list(filter(lambda t: t[1 if iscolumn else 0] == index, tiles.keys()))

def get_tiles_row(tiles, column):
    return get_tiles(tiles, column, False)

def get_tiles_column(tiles, column):
    return get_tiles(tiles, column, True)

def transpose_list(tiles):
    return list(map(lambda t: transpose(t), tiles))

def transpose(tile):
    return (tile[1], tile[0])

def move(current, move):
    return (current[0] + move[0], current[1] + move[1])

def print_collected_tiles(tiles, current, next_tiles):
    for cy in range(get_max_rows(tiles)[0] + 1):
        for cx in range(get_max_columns(tiles)[1] + 1):
            tile = (cy,cx)
            if(tile == current):
                print('C', end='')
            elif(tile in next_tiles):
                if(tiles[tile] == wall):
                    print(wall, end='')
                else: 
                    tile_keys = list(next_tiles.keys())
                    # print(tile_keys.index(tile)%10, end='')
                    print(tile_keys.index(tile)//50, end='')
                    # print(directions_show[next_tiles[tile]], end='')
            elif(tile in tiles):
                print(tiles[tile], end='')
            else:
                print(' ',end='')
        print()

def print_tiles(tiles):
    for cy in range(get_max_rows(tiles)[0] + 1):
        for cx in range(get_max_columns(tiles)[1] + 1):
            if((cy,cx) in tiles):
                print(tiles[(cy,cx)], end='')
            else:
                print(' ',end='')
        print()