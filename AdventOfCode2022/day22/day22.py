# The final password is the sum of 1000 times the row, 4 times the column, and the facing.
from utils import *
from constants import *

def process_tiles(file):
    tiles = {}
    with open(file) as input:
        lines = [l for l in input]
        commands = lines[-1]
        y = 0
        for l in lines:
            y+=1
            if(l != '\n'):
                x = 0
                for tile in list(l):
                    x+=1
                    if(tile == ' ' or tile == '\n'):
                        pass
                    else:
                        tiles[(y,x)] = tile
            else:
                break
    return tiles, commands

def get_first_tile(tiles):
    first_tile = None
    i = 1
    while(first_tile == None):
        first_row = get_tiles_row(tiles, i)
        first_row.sort(key=lambda t: t[1])
        for tile in first_row:
            if(tiles[tile] == empty):
                first_tile = tile
                break
        i+=1
    return first_tile

def process_commands(commands):
    i = 0
    processed_commands = []
    for c in list(commands):
        if(c not in dir_changes):
            i = i*10 + int(c)
        else:
            processed_commands.append(i)
            processed_commands.append(c)
            i = 0
    processed_commands.append(i)
    return processed_commands

def get_next_tiles_1(current_dir, tiles, current_tile):
    match(current_dir):
        case 0: return get_tiles_row(tiles, current_tile[0])
        case 1: return get_tiles_column(tiles, current_tile[1])
        case 2: 
            next_tiles = get_tiles_row(tiles, current_tile[0])
            next_tiles.reverse()
            return next_tiles
        case 3: 
            next_tiles = get_tiles_column(tiles, current_tile[1])
            next_tiles.reverse()
            return next_tiles

def get_next_tiles_2(current_dir, tiles, current_tile, grid_size):
    next_tiles = get_next_tiles_1(current_dir, tiles, current_tile)
    tiles_with_directions = list(map(lambda t: (t, current_dir), next_tiles))
    # print(f"next_tiles {next_tiles}")
    grid_mod = grid_size - 1
    if(len(next_tiles) < 4 * grid_size):
            block_mod = (current_tile[0] - 1 if current_dir in[0,2] else current_tile[1] - 1) % grid_size

            start_current_block_x = (min(next_tiles, key=lambda t: t[1])[1] - 1) // grid_size * grid_size + 1
            start_next_block_x = (max(next_tiles, key=lambda t: t[1])[1] - 1) // grid_size * grid_size + grid_size + 1
            start_current_block_y = (min(next_tiles, key=lambda t: t[0])[0] - 1) // grid_size * grid_size + 1
            start_next_block_y = (max(next_tiles, key=lambda t: t[0])[0] - 1) // grid_size * grid_size + grid_size + 1

            if(current_dir in [1,3]):
                tmp_current = start_current_block_x
                tmp_next = start_next_block_x
                start_current_block_x = start_current_block_y
                start_next_block_x = start_next_block_y
                start_current_block_y = tmp_current
                start_next_block_y = tmp_next

            # print(block_mod, start_current_block_x, start_next_block_x, start_current_block_y, start_next_block_y)

            prev_row_index = start_current_block_y - 1 - grid_size - block_mod
            prev_row = get_next_tiles_1((current_dir - 2) % 4, tiles, (prev_row_index, prev_row_index))
            # print(f"prev_row {prev_row_index} = {prev_row}")
            if(current_dir in [1,3]):
                prev_row = transpose_list(prev_row)
            matching_prev_row = any(t[1] >= start_current_block_x and t[1] < start_next_block_x for t in prev_row)
            if(not matching_prev_row):
                prev_row = []
            if(current_dir in [1,3]):
                prev_row = transpose_list(prev_row)
            prev_row_with_dir = list(map(lambda t: (t, (current_dir - 2) % 4), prev_row))

            dir_change = -1 if(current_dir in [1,3]) else 1
            prev_col_index_under = start_current_block_x - 1 - block_mod
            prev_col = get_next_tiles_1((current_dir - dir_change) % 4, tiles, (prev_col_index_under, prev_col_index_under))
            # print(f"prev_col {prev_col_index_under} = {prev_col}")
            if(current_dir in [1,3]):
                prev_col = transpose_list(prev_col)
            prev_col_with_dir = []
            if(prev_col):
                prev_col_end = max(prev_col, key = lambda t: t[0])
                prev_col_start = min(prev_col, key = lambda t: t[0])
                if( prev_col and prev_col_end[0] == start_current_block_y - 1 ):
                    if(current_dir in [0]):
                        prev_col.reverse()
                elif(prev_col and prev_col_start[0] == start_next_block_y):
                    prev_col = list(map(lambda t: (t[0],start_current_block_x - 1 - (grid_mod - block_mod)), prev_col))
                    # if(current_dir in [0, 1]):
                    #     prev_col.reverse()
                    dir_change = -1 * dir_change
                else:
                    prev_col = []
                if(current_dir in [1,3]):
                    prev_col = transpose_list(prev_col)
                prev_col_with_dir = list(map(lambda t: (t, (current_dir + dir_change) % 4), prev_col))
            

            dir_change = -1 if(current_dir in [1,3]) else 1
            next_col_index = start_next_block_x + block_mod
            next_col = get_next_tiles_1((current_dir + dir_change) % 4,tiles, (next_col_index, next_col_index))
            # print(f"next_col {next_col_index} = {next_col}")
            if(current_dir in [1,3]):
                next_col = transpose_list(next_col)
            next_col_with_dir = []
            if(next_col):
                next_col_end = max(next_col, key = lambda t: t[0])
                next_col_start = min(next_col, key = lambda t: t[0])
                if( next_col and next_col_end[0] == start_current_block_y - 1 ):
                    dir_change = -1 * dir_change
                elif(next_col and next_col_start[0] == start_next_block_y):
                    next_col = list(map(lambda t: (t[0],start_next_block_x + grid_mod - block_mod), next_col))
                else:
                    next_col = []
                if(current_dir in [1,3]):
                    next_col = transpose_list(next_col)
                next_col_with_dir = list(map(lambda t: (t, (current_dir + dir_change) % 4), next_col))
            

            next_row_index = start_next_block_y + grid_size + grid_mod - block_mod
            next_row = get_next_tiles_1((current_dir + 2) % 4, tiles, (next_row_index, next_row_index))
            if(current_dir in [1,3]):
                next_row = transpose_list(next_row)
            # print(f"next_row {next_row_index} = {next_row}")
            matching_next_row = any(t[1] >= start_current_block_x and t[1] < start_next_block_x for t in next_row)
            if(not matching_next_row):
                next_row = []
            if(current_dir in [1,3]):
                next_row = transpose_list(next_row)
            next_row_with_dir = list(map(lambda t: (t, (current_dir + 2) % 4), next_row))


            prev = (prev_row_with_dir + prev_col_with_dir) if current_dir in [0,1] else (prev_col_with_dir + prev_row_with_dir) 
            next = (next_col_with_dir + next_row_with_dir) if current_dir in [0,1] else (next_row_with_dir + next_col_with_dir) 
            tiles_with_directions = (prev + tiles_with_directions + next) if current_dir in [0,2] else (next + tiles_with_directions + prev)
    return tiles_with_directions


tiles, commands = process_tiles('day22')
processed_commands = process_commands(commands)

current_tile = get_first_tile(tiles)
current_dir = 0
grid_size = 50

# current_dir = 3
# current_tile = (6, 7)
# next_tiles = get_next_tiles_2(current_dir, tiles, current_tile, 4)
# next_tiles = {t[0]: t[1] for t in next_tiles}
# print(len(next_tiles), next_tiles)
# print_collected_tiles(tiles, current_tile, next_tiles)

for c in range(0, len(processed_commands) + 1, 2):
    print(f"STEP {c//2 + 1} - {current_tile}")
    print(c, current_dir, processed_commands[c], processed_commands[c + 1] if c + 1 < len(processed_commands) else '')
    next_tiles = get_next_tiles_2(current_dir, tiles, current_tile, grid_size)
    next_tiles = {t[0]: t[1] for t in next_tiles}
    # print(next_tiles)
    if(len(next_tiles) != 4*grid_size):
        start_tile = list(next_tiles.keys())[-1]
        next_tiles_update = get_next_tiles_2(next_tiles[start_tile], tiles, start_tile, grid_size)

        for tile in next_tiles_update:
            next_tiles[tile[0]] = tile[1]

        if(len(next_tiles) != 4*grid_size):
            start_tile = list(next_tiles.keys())[0]
            next_tiles_update = get_next_tiles_2(next_tiles[start_tile], tiles, start_tile, grid_size)

            for tile in next_tiles_update:
                next_tiles[tile[0]] = tile[1]

            if(len(next_tiles) != 4*grid_size):
                print_collected_tiles(tiles, current_tile, next_tiles)
                raise Exception(f"not adequate number of possible next tiles: {len(next_tiles)} \n{next_tiles}")

    tile_keys = list(next_tiles.keys())
    current_tile_index = tile_keys.index(current_tile)
    for _ in range(processed_commands[c]):
        current_tile_index+=1
        next_tile = tile_keys[current_tile_index%len(tile_keys)]
        if(tiles[next_tile] == wall):
            break
        current_tile = next_tile
        # print(current_tile)
    if(c+1 < len(processed_commands)):
        current_dir = (next_tiles[current_tile] + dir_changes[processed_commands[c + 1]]) % 4

print(current_tile, current_dir)

print( current_tile[0] * 1000 + current_tile[1] * 4 + current_dir)


