```toml
name = 'Stwórz NPC'
description = 'Wtorzy postać niegrywalną'
method = 'POST'
url = 'http://localhost:8888/api/v1/authorized/cpRed/characters/createNpc'
sortWeight = 2000000
id = 'a3844127-40cb-42cf-b4a7-15395f9e8b2f'

[body]
type = 'JSON'
raw = '''
{
  "game": {
    "id": 1
  },
  "name": "Adam Smasher",
  "nickname": "Smasher",
  "expAll": 5000,
  "expAvailable": 0,
  "cash": 100000
}'''
```
