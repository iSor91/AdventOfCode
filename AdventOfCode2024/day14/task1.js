
const fs = require('fs')

const data = fs.readFileSync('input', 'utf-8').split('\n').filter(l => l.length !== 0);

const roomSize = [101, 103]
// const roomSize = [11, 7]

const moveRobot = ([x, y, vx, vy]) => {
  const newPosX = (x + vx) % roomSize[0]
  const newPosY = (y + vy) % roomSize[1]
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

const seconds = 100

// robots = robots.slice(0, 1)

for (let i = 0; i < seconds; i++) {
  printBots(robots)
  robots = robots.map(r => {
    const newPos = moveRobot(r)
    console.log(newPos)
    return newPos
  })
  console.log()
}

printBots(robots)
console.log(robots)

const firstQuad = robots.filter(r => r[0] < (roomSize[0] - 1) / 2 && r[1] < (roomSize[1] - 1) / 2)
const secondQuad = robots.filter(r => r[0] > (roomSize[0] - 1) / 2 && r[1] < (roomSize[1] - 1) / 2)
const thirdQuad = robots.filter(r => r[0] < (roomSize[0] - 1) / 2 && r[1] > (roomSize[1] - 1) / 2)
const forthQuad = robots.filter(r => r[0] > (roomSize[0] - 1) / 2 && r[1] > (roomSize[1] - 1) / 2)

console.log(firstQuad.length, secondQuad.length, thirdQuad.length, forthQuad.length)

console.log(firstQuad.length * secondQuad.length * thirdQuad.length * forthQuad.length)
