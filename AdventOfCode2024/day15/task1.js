

const fs = require('fs')

const data = fs.readFileSync('input', 'utf-8').split('\n');

const directionMap = {
  'v': [1, 0],
  '^': [-1, 0],
  '<': [0, -1],
  '>': [0, 1]
}

const map = []
const objects = []
const control = []
let bot

let mapOver = false

data.forEach(l => {

  if (!mapOver) {
    if (l.length === 0) {
      mapOver = true
    } else {
      map.push(l)

    }
  } else {
    if (l.length > 0)
      control.push(l)
  }
});


map.forEach((l, i) => {
  l.split('').forEach((c, j) => {
    if (c === '#') {
      objects.push({ type: '#', pos: [i, j] })
    } else if (c === 'O') {
      objects.push({ type: 'O', pos: [i, j] })
    } else if (c === '@') {
      bot = { type: '@', pos: [i, j] }
      objects.push(bot)
    }
  })
})
const instructions = control.flatMap(l => l.split('').map(d => directionMap[d]))
const instructionsString = control.flatMap(l => l.split(''))

console.log(instructions)
console.log(objects, bot)

const add = (a, b) => {
  return [a[0] + b[0], a[1] + b[1]]
}

const move = (obj, dir) => {
  if (obj.type === '#') {
    return false
  }
  const newPos = add(obj.pos, dir)
  const pushedObj = objects.filter(o => o.pos[0] === newPos[0] && o.pos[1] === newPos[1])[0]
  if (pushedObj) {
    if (move(pushedObj, dir)) {
      obj.pos = newPos
      return true
    }
    return false
  }
  obj.pos = newPos
  return true
}

const printMap = () => {
  for (let i = 0; i < map.length; i++) {
    const line = []
    for (let j = 0; j < map[0].length; j++) {
      const existingObj = objects.filter(o => o.pos[0] === i && o.pos[1] === j)[0]
      if (existingObj) {
        line.push(existingObj.type)
      } else {
        line.push('.')
      }
    }
    console.log(line.join(''))
  }
  console.log()
}

instructions.forEach((i, index) => {
  move(bot, i)
  console.log(index, instructionsString[index])
  // printMap()
})

console.log(bot)

const result = objects.filter(o => o.type === 'O').map(o => o.pos[0] * 100 + o.pos[1]).reduce((p, c) => p + c, 0)

console.log(result)
