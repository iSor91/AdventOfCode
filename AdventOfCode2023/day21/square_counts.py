prev = 0
odd,even = 0,0
for i in range(1,202302):
    tmp = (i*i*2)-(i*2)+1
    if(i%2 == 1):
        even+=(tmp-prev)
    else:
        odd+=(tmp-prev)
    prev = tmp
    print(even, odd)


