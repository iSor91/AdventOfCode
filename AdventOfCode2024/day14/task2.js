
const fs = require('fs')

const data = fs.readFileSync('input', 'utf-8').split('\n').filter(l => l.length !== 0);

const roomSize = [101, 103]
// const roomSize = [11, 7]

const moveRobot = ([x, y, vx, vy], count) => {
  const newPosX = (x + vx * count) % roomSize[0]
  const newPosY = (y + vy * count) % roomSize[1]
  return [newPosX < 0 ? roomSize[0] + newPosX : newPosX,
  newPosY < 0 ? roomSize[1] + newPosY : newPosY,
    vx, vy]

}

const printBots = (bots) => {
  for (let y = 0; y < roomSize[1]; y++) {
    const row = []
    for (let x = 0; x < roomSize[0]; x++) {
      if (bots.filter(r => r[0] === x && r[1] === y)[0]) {
        row.push('#')
      }
      else {
        row.push('.')
      }
    }
    console.log(row.join(''))
  }
}

let robots = data.map(l => {
  [x, y, vx, vy] = l.match(/-?\d+/g).map(n => +n);
  return [x, y, vx, vy]
})

console.log(robots)

// let seconds = 88
const seconds = 8168

// robots = robots.slice(0, 1)

for (let i = 0; i < seconds; i++) {
  robots = robots.map(r => {
    const newPos = moveRobot(r, 1)
    return newPos
  })
}

printBots(robots)

// const attmepts = 80
//
// for (let i = 0; i < attmepts; i++) {
//   robots = robots.map(r => {
//     const newPos = moveRobot(r, 101)
//     return newPos
//   })
//   seconds += 101
//   if (i == 79) {
//     console.log(seconds)
//     printBots(robots)
//     console.log()
//   }
// }
