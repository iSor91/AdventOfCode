
const fs = require('fs')

const [towelsString, patternsString] = fs.readFileSync('input', 'utf-8').split('\n\n');

const alltowels = towelsString.split(', ')
const patterns = patternsString.split('\n').filter(pattern => pattern.length !== 0)

console.log(alltowels)

const countRecur = (towels, n, pattern, memo) => {
  // If sum is 0 then there is 1 solution
  // (do not include any coin)
  if (pattern.length === 0) {
    return 1;
  }
  // 0 ways in the following two cases
  if (n === 0) return 0;

  // If the subproblem is previously calculated then
  // simply return the result
  if (memo[towels[n - 1]][pattern]) return memo[towels[n - 1]][pattern];

  // count is sum of solutions (i)
  // including coins[n-1] (ii) excluding coins[n-1]
  const remainingPattern = pattern.substring(towels[n - 1].length)
  const matchingTowels = alltowels.filter(t => remainingPattern.startsWith(t))
  // console.log(remainingPattern, matchingTowels)

  memo[towels[n - 1]][pattern] =
    countRecur(matchingTowels, matchingTowels.length, remainingPattern, memo) +
    countRecur(towels, n - 1, pattern, memo);

  return memo[towels[n - 1]][pattern]

}

const count = (towels, pattern, memo) => {
  const matchingTowels = towels.filter(t => pattern.startsWith(t))
  return countRecur(matchingTowels, matchingTowels.length, pattern, memo);
}


const possible = []
let sum = 0
for (let pattern of patterns) {
  const memo = {}
  alltowels.forEach(t => {
    memo[t] = {}
  });
  const result = count(alltowels, pattern, memo)
  if (result !== 0) {
    console.log(result)
    possible.push(pattern)
    sum += result
  }
}
console.log(possible.length)
console.log(sum)

