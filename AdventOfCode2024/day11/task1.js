
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

const transform = (stones) => {
  return stones.flatMap(s => {
    return transformStone(s)
  })
}

const blinks0 = 36
const remainingStoneCounts = {}
for (let s = 0; s < 10; s++) {
  let transformed = [s]
  const stoneCounts = {}
  for (let i = 0; i < blinks0; i++) {
    stoneCounts[i] = transformed.length
    transformed = (transform(transformed))
    // console.log(i, transformed)
  }
  stoneCounts[blinks0] = transformed.length
  remainingStoneCounts[s] = stoneCounts
}

console.log(remainingStoneCounts)

const blinks = 75

let stoneCount = []
let count = 0
stones.forEach(stone => {
  let transformed = [stone]
  console.log(transformed)
  for (let i = 0; i < blinks; i++) {
    transformed = (transform(transformed))
    const currentRemainingSteps = blinks - i
    if (currentRemainingSteps <= blinks0) {
      for (let s = 0; s < 10; s++) {
        transformed.filter(t => t === s).map(s => remainingStoneCounts[s][currentRemainingSteps]).forEach(t => stoneCount.push(t))
        transformed = transformed.filter(t => t !== s)
      }
    }
    console.log(i, transformed.length, stoneCount.length)
  }
  count += stoneCount.reduce((previous, current) => {
    return previous + current
  }, 0)
  count += transformed.length
  stoneCount = []
  console.log(`current stones: ${count}`)
})


console.log(count)
