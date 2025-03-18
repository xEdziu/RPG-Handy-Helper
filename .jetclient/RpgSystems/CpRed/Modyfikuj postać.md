```toml
name = 'Modyfikuj postać'
description = 'Pozwala na modyfikacje danych postaci'
method = 'PUT'
url = 'http://localhost:8888/api/v1/authorized/games/cpRed/characters/update/1'
sortWeight = 4000000
id = '0a04e0cf-9e18-497f-afa3-8d830117dc72'

[body]
type = 'JSON'
raw = '''
{
  "user": {
    "id": 1
  },
  "name": "Adam Smasher",
  "nickname": "Smasher",
  "expAll": 5000,
  "expAvailable": 0,
  "cash": 100000
}'''
```
