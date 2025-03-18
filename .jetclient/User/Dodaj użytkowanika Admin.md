```toml
name = 'Dodaj użytkowanika Admin'
description = 'Dodaje nowego usera '
method = 'POST'
url = 'http://localhost:8888/api/v1/authorized/admin/user/create'
sortWeight = 1000000
id = '19d129a1-0727-45a7-b3c8-2a7a9209fa25'

[body]
type = 'JSON'
raw = '''
{
  "username": "exampleUser",
  "firstName": "John",
  "surname": "Doe",
  "email": "john.doe@example.com",
  "password": "securePassword123!",
  "role": "ROLE_USER",
  "oAuthId": "1234567890",
  "createdAt": "2023-10-01T12:00:00Z"
}'''
```
