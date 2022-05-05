import sys
import math

def fprint(*a, **k):
    print(*a, **k, file=sys.stderr, flush=True)


width, height = [int(i) for i in input().split()]


while True:
    x, y = [int(i) for i in input().split()]
    opponent_x, opponent_y = [int(i) for i in input().split()]
    nb_walls = int(input())
    fprint(f"{nb_walls=}")
    for i in range(nb_walls):
        ax, ay, bx, by = [int(j) for j in input().split()]

    fprint(x, y)

    if x == 0:
        print(x+1, y, "R")
    else:
        print(x-1, y, "L")
