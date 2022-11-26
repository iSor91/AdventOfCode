input=open('day4.txt')

# # task1
# def process_word(w):
#     return w

# task2
def process_word(w):
    characters=list(w)
    characters.sort()
    return ''.join(characters)
    

valid = 0
for i in input:
    d = {}
    words = i.split()
    for w in words:
        word=process_word(w)
        count = d.get(word,0)
        d[word]=count+1
    if( not any(c > 1 for c in d.values())):
        valid+=1

print(valid)
