```toml
name = 'Dodaj użytkownika do gry'
description = 'Dodaje użytkownika do gry z odpowiednią rolą'
method = 'POST'
url = 'http://localhost:8888/api/v1/authorized/game/addUserToGame'
sortWeight = 1500000
id = '13983dae-c902-43d1-9f6c-02d951a7a4fe'

[body]
type = 'JSON'
raw = '''
{
  "userId": 2,
  "gameId": 1,
  "role": "PLAYER"
}'''
```
