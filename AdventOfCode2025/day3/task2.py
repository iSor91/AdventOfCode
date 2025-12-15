
file = open('input', 'r')
lines = file.readlines()

sum = 0
size = 12

for line in lines:
    chars = list(line.strip())

    nums = [int(chars[i]) for i in range(len(chars)-1, len(chars)-size-1, -1)]
    print(nums)

    for i in range(len(chars) - size - 1, -1, -1):
        print(chars[i])
        i_num = int(chars[i])
        if (i_num >= nums[size-1]):
            switch_from = 0
            for j in range(size-2, -1, -1):
                if (nums[j] > nums[j+1]):
                    switch_from = j+1
                    break

            for j in range(switch_from, size-1):
                nums[j] = nums[j+1]
            nums[size-1] = i_num

    print(nums)
    num = 0
    for i in range(0, size):
        print(nums[i], i)
        num += (nums[i] * pow(10, i))

    print(num)
    sum += num

print(sum)
