package main

import (
	"fmt"
	"os"
	"path/filepath"
	"strconv"
	"strings"
)

func main() {
	task2()
}

func task1() {
	path := filepath.Join("input")
	dat, err := os.ReadFile(path)
	if err != nil {
		panic(err)
	}

	start := 50
	current := start

	sum := 0

	for i, line := range strings.Split(string(dat), "\n") {
		if len(line) == 0 {
			break
		}
		// fmt.Print(i, string(line), "\n")
		parts := strings.SplitN(line, "", 2)
		dials, _ := strconv.Atoi(parts[1])

		if parts[0] == "R" {
			current = (current + dials) % 100
		}
		if parts[0] == "L" {
			current = (current - dials) % 100
		}
		if current < 0 {
			current = 100 + current
		}
		if current == 0 {
			sum++
		}
		fmt.Println(i, ": ", current, " -> ", parts[0], " - ", parts[1])

	}

	fmt.Println(sum)
}

func task2() {
	path := filepath.Join("test2")
	dat, err := os.ReadFile(path)
	if err != nil {
		panic(err)
	}

	start := 50
	current := start

	sum := 0

	for _, line := range strings.Split(string(dat), "\n") {
		if len(line) == 0 {
			break
		}
		// fmt.Print(i, string(line), "\n")
		parts := strings.SplitN(line, "", 2)
		clicks, _ := strconv.Atoi(parts[1])
		start = current

		if parts[0] == "R" {
			for range clicks {
				current++
				if current%100 == 0 {
					sum++
				}
			}
		}
		if parts[0] == "L" {
			for range clicks {
				current--
				if current%100 == 0 {
					sum++
				}
			}
		}

		current = current % 100
		if current < 0 {
			current = 100 + current
		}

		fmt.Println(sum, " : ", current, " <- ", parts[0], " - ", parts[1])
	}

	fmt.Println(sum)
}

//6457 nem jo
//5951 nem jo
//6254 jo, brute force ftw...
