

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
      objects.push({ type: '#', typeEnd: '#', pos: [[i, 2 * j], [i, 2 * j + 1]] })
    } else if (c === 'O') {
      objects.push({ type: '[', typeEnd: ']', pos: [[i, 2 * j], [i, 2 * j + 1]] })
    } else if (c === '@') {
      bot = { type: '@', pos: [[i, 2 * j], [i, 2 * j]] }
    }
  })
})
const instructions = control.flatMap(l => l.split('').map(d => directionMap[d]))
const instructionsString = control.flatMap(l => l.split(''))

// console.log(instructions)
// console.log(objects, bot)

const add = (a, b) => {
  return [a[0] + b[0], a[1] + b[1]]
}

const move = (obj, dir) => {
  if (obj.type === '#') {
    return [false]
  }
  const newPosStart = add(obj.pos[0], dir)
  const newPosEnd = add(obj.pos[1], dir)

  const pushedObjByStart = objects.filter(o =>
    o !== obj &&
    ((o.pos[0][0] === newPosStart[0]
      && o.pos[0][1] === newPosStart[1])
      || (o.pos[1][0] === newPosStart[0]
        && o.pos[1][1] === newPosStart[1]))
  )[0]
  const pushedObjByEnd = objects.filter(o =>
    o !== obj &&
    ((o.pos[0][0] === newPosEnd[0]
      && o.pos[0][1] === newPosEnd[1])
      || (o.pos[1][0] === newPosEnd[0]
        && o.pos[1][1] === newPosEnd[1]))
  )[0]

  // console.log(obj, pushedObjByStart, pushedObjByEnd)
  const results = []

  if (pushedObjByStart) {
    const moveRes = move(pushedObjByStart, dir)
    // console.log(moveRes)
    results.push(...moveRes)
  } else {
    results.push([true])
  }

  if (pushedObjByEnd && pushedObjByStart !== pushedObjByEnd) {
    const moveRes = move(pushedObjByEnd, dir)
    // console.log(moveRes)
    results.push(...moveRes)
  } else {
    results.push([true])
  }

  return [[true, newPosStart, newPosEnd, obj], ...results]
}

const printMap = () => {
  for (let i = 0; i < map.length; i++) {
    const line = []
    for (let j = 0; j < map[0].length * 2; j++) {
      const existingObjStart = objects.filter(o => o.pos[0][0] === i && o.pos[0][1] === j)[0]
      const existingObjEnd = objects.filter(o => o.pos[0][0] === i && o.pos[1][1] === j)[0]
      if (existingObjStart) {
        line.push(existingObjStart.type)
      } else if (existingObjEnd) {
        line.push(existingObjEnd.typeEnd)
      } else if (bot.pos[0][0] === i && bot.pos[0][1] === j) {
        line.push(bot.type)
      } else {
        line.push('.')
      }
    }
    console.log(line.join(''))
  }
  console.log()
}

printMap()

instructions.forEach((i, index) => {
  console.log(index, instructionsString[index])
  const res = move(bot, i)
  // console.log(res)
  if (res.some(r => !r[0])) {
    'cannot move'
  } else {
    res.filter(r => r.length === 4).forEach(r => {
      r[3].pos[0] = r[1]
      r[3].pos[1] = r[2]
    })
  }
  console.log(bot)
  // printMap()
})

printMap()
console.log(bot)

const result = objects.filter(o => o.type === '[').map(o => o.pos[0][0] * 100 + o.pos[0][1]).reduce((p, c) => p + c, 0)

console.log(result)
