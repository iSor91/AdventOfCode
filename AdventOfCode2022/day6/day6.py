mrk_len = 4 # task1
msg_len = 14 # task2

with open('day6') as signals:
    l = mrk_len
    for signal in signals:
        for i in range(l, len(signal)):
            data = set(signal[i-l:i])
            if(len(data) == l):
                print(data, i)
                break