input=325489

n = 0
for i in range(1,1000,2):
    if(i*i > input):
        n = i-2
        break

print(f"inner grid size: {n}*{n}")

x=int(n/2)
y=x
print(f"starting space: {x}-{y}")

current = n*n
quarter = 0


print(f"starting number: {current}")
while(current+n<input):
    current+=n+1
    quarter+=1
    if(quarter==1):
        x+=1
    if(quarter==2):
        y+=1

print(f"input in quarter {quarter}")

if(quarter == 0 and current != input):
    x+=1
    current+=1

offset = 0
print(f"counting offset from {current}")

while(current+offset<input):
    offset+=1

print(f"the offset is {offset}")

print(f"{x}-{y}")
print(f"the first coordinate is {x}")
print(f"the other coordinate is {abs(y - offset)}")

print(f"required steps to return to start: {x + abs(y - offset)}")
