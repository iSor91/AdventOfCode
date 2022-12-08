def find_index(current, list):
    try:
        return list.index(next(l for l in list if l >= current)) + 1
    except StopIteration:
        return len(list)


with open('day8') as treelines:

    rows = [list(map(int,list(l.strip()))) for l in treelines]
    columns = [[rows[i][c] for i in range(len(rows))] for c in range(len(rows[0]))]

    visible = 0
    max_visibility = 0
    for i in range(len(rows[0])):
        for j in range(len(rows)):
            current = rows[j][i]
            bigger_left = [x for x in rows[j][0:i] if x >= current]
            bigger_right = [x for x in rows[j][i+1:] if x >= current]
            bigger_up = [x for x in columns[i][0:j] if x >= current]
            bigger_down = [x for x in columns[i][j+1:] if x >= current]
            # print(bigger_up, bigger_left, current, bigger_right, bigger_down)

            if(not(bigger_up and bigger_left and bigger_right and bigger_down)):
                visible += 1
            
            first_bigger_left = find_index(current, list(reversed(rows[j][:i])))
            first_bigger_right = find_index(current, rows[j][i+1:]) 
            first_bigger_up = find_index(current, list(reversed(columns[i][:j])))
            first_bigger_down = find_index(current, columns[i][j+1:]) 
            visibility = first_bigger_up * first_bigger_left * first_bigger_right * first_bigger_down
            if(visibility > max_visibility):
                max_visibility = visibility

    print(visible)
    print(max_visibility)

