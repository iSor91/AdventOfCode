package main

import (
	"fmt"
	"os"
	"path/filepath"
	"strconv"
	"strings"
)

func main() {
	task1()

}

func task1() {
	path := filepath.Join("input")
	dat, err := os.ReadFile(path)
	if err != nil {
		panic(err)
	}

	ranges := strings.Split(string(dat), ",")
	sum := 0

	for _, r := range ranges {
		if len(r) == 0 {
			break
		}

		boundaries := strings.Split(r, "-")

		if len(boundaries[0])%2 != 0 && len(boundaries[1])%2 != 0 && len(boundaries[0]) == len(boundaries[1]) {
			fmt.Println(r)
			continue
		}

		fmt.Print(strings.TrimSpace(r), " check")

		splitAt := len(boundaries[0]) / 2
		min, _ := strconv.Atoi(strings.TrimSpace(boundaries[0]))
		max, _ := strconv.Atoi(strings.TrimSpace(boundaries[1]))
		fmt.Print(" - max", max)

		start := boundaries[0][:splitAt]
		if len(boundaries[0])%2 != 0 {
			var sb strings.Builder
			sb.WriteString("1")
			for range len(start) {
				sb.WriteString("0")
			}
			start = sb.String()
		}

		fmt.Println(" - start", start)

		current, _ := strconv.Atoi(start)

		//
		for {
			var sb strings.Builder
			sb.WriteString(strconv.Itoa(current))
			sb.WriteString(strconv.Itoa(current))
			check, _ := strconv.Atoi(sb.String())

			if check > max {
				break
			}

			fmt.Println("   ", check)

			if check >= min {
				sum += check
			}
			current++
		}

	}

	fmt.Println(sum)

	//1227775554
	//1228341218

}
