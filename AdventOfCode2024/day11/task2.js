
const fs = require('fs')

const data = fs.readFileSync('input', 'utf-8')
  .split('\n')
  .filter(l => l.length !== 0)

const stones = data.flatMap(l => l.split(' ').map(s => +s))
console.log(stones)

const transformStone = (stone) => {
  if (stone === 0) {
    return [1]
  }
  if (`${stone}`.length % 2 === 0) {
    const s = `${stone}`
    return [+s.substring(0, s.length / 2), +s.substring(s.length / 2)]
  }
  return [stone * 2024]
}

let currentStones = {}

stones.forEach(s => {
  let count = currentStones[+s] ?? 0
  count++
  currentStones[+s] = count
})
console.log(currentStones)


for (let i = 0; i < 75; i++) {
  const tmpStones = {}
  Object.keys(currentStones).forEach(stone => {
    const newStones = transformStone(+stone)
    const originalCount = currentStones[+stone]
    newStones.forEach(ns => {
      let newStoneCount = tmpStones[ns] ?? 0
      newStoneCount += originalCount
      tmpStones[ns] = newStoneCount
    })
  })
  currentStones = tmpStones

  console.log(currentStones)
}

console.log(Object.keys(currentStones).reduce((p, c) => p + currentStones[c], 0))
