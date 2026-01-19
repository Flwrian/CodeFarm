function update()
    print("Script Lua démarre!")
    for i = 1, 5 do
        move("right")
        harvest()
    end
    print("Script terminé")
end