for y in `seq 0 20 0`
  do for x in `seq 0 20 140`
    do convert "waves.svg[20x20+$x+$y]" "wave-$y-$x.png"
  done;
done;
