
const fs = require('fs')

const data = fs.readFileSync('input', 'utf-8').split('\n').filter(l => l.length !== 0)


console.log(data)


const availableOperators = [
  {
    sign: '+',
    apply: (a, b) => a + b
  },
  {
    sign: '*',
    apply: (a, b) => a * b
  },
  {
    sign: '||',
    apply: (a, b) => +`${a}${b}`
  }
]


const equations = (data.map((l) => {
  return l.split(new RegExp(": | ")).map(c => +c);
}))

const resolvedEquations = equations.map(numbers => {
  const expectedResult = numbers[0]
  const usableNumbers = numbers.slice(1);

  let currentValues = [usableNumbers[0]]

  for (let i = 1; i < usableNumbers.length; i++) {
    currentValues = currentValues.flatMap(a => {
      const b = usableNumbers[i]
      return availableOperators.map(o => o.apply(a, b))
    })

    currentValues = currentValues.filter(v => v <= expectedResult)
    console.log(currentValues.length)
  }


  return currentValues.filter(v => v === expectedResult)
});

console.log(resolvedEquations.filter(e => e.length !== 0).reduce((p, c) => p + c[0], 0))

