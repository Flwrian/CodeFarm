function update()
  while true do
    move("right")
    move("up")
    move("left")
    move("down")
    move("right")
    for i = 1, 5 do
      turnRight()
    end
  end
end