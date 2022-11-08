# Spring Boot Twitter

## Gradle based spring boot application which provide APIs to implement few features of twitter using test driven development.

## Features of the Application
    - Create User
    - Read Users by User id
    - Post a Tweet
    - Read Tweets of User
    - Read Tweet of User by Tweet Id
    - Follow a User
    - Read Followers of a User
    - Read Follows of a User
    - Unfollow a User

## User APIs

### Create a User

* Request
```
POST /users 
Host: localhost:3000
Content-Type: application/json
{
    "id": 1,
    "name": “Ironman”,
}
```
* Response
```
Status code: 201 Created
Body: 
{
    "data": {
        "id": 1
    }
}
```

### If we try to provide empty User’s name in the request body

* Request
```
POST /users 
Host: localhost:3000
Content-Type: application/json
{
    "id": 1
}
```
* Response
```
Status code: 400 Bad Request
Body:
{
    "error": {
        "message": "User name should not be empty!"
    }
}
```

### If the User is already exists

* Request
```
POST /users 
Host: localhost:3000
Content-Type: application/json
{
    "id": 1,
    "name": “Ironman”,
}
```
* Response
```
Status code: 400 Bad Request
Body:
{
    "error": {
        "message": "User is already exists with id = 1”
    }
}
```

### Get User details by User Id

* Request
```
GET /users/{1}
Host: localhost:3000
```
* Response
```
Status code: 200 OK
Body:
{
    "data": {
        "id": 1,
        "name": "Ironman"
    }
}
```

### If we try to provide not existed User Id in the path url

* Request
```
GET /users/{1}
Host: localhost:3000
```
* Response
```
Status code: 404 Not Found
Body:
{
    "error": {
        "message": "User not found with id = 1"
    }
}
```

## Tweet APIs

### Post a Tweet

* Request
```
POST /users/{1}/tweets
Host: localhost:3000
Content-Type: application/json
{
    "description": "This is my first tweet!”
}
```
* Response
```
Status code: 201 Created
Body:
{
    "data": {
        "tweet_id": 1
    }
}
```

### If we try to provide empty Tweet’s description in the request body

* Request
```
POST /users/{1}/tweets
Host: localhost:3000
Content-Type: application/json
{
    "description": "”
}
```
* Response
```
Status code: 400 Bad Request
Body:
{
    "error": {
          "message": "Description should not be null or empty!"
     }
}
```

### If we try to provide not existed User Id in the path url

* Request
```
POST /users/{1}/tweets
Host: localhost:3000
Content-Type: application/json
{
    "description": “This is my first tweet!”
}
```
* Response
```
Status code: 404 Not Found
Body:
{
    "error": {
        "message": "User not found with id = 1”
    }
}
```

### Get Tweets by User Id

* Request
```
GET /users/{1}/tweets
Host: localhost:3000
```
* Response
```
Status code: 200 OK
Body:
{
    "data": [
        {
            "id": 22,
            "description": "This is my first tweet!",
            "authorId": 1,
            "authorName": "Ironman"
        },
        {
            "id": 23,
            "description": "This is my second tweet!",
            "authorId": 1,
            "authorName": "Ironman"
        }
    ]
}
```

### Get Tweet details of a User by Tweet Id

* Request
```
GET /users/{1}/tweets/{22}
Host: localhost:3000
```
* Response
```
Status code: 200 OK
Body:
{
    "data": {
        "id": 22,
        "description": "This is my first tweet!",
        "authorId": 1,
        "authorName": "Ironman"
    }
}
```

### If we provide not existed Tweet Id in the path url

* Request
```
GET /users/{1}/tweets/{220}
Host: localhost:3000
```
* Response
```
Status code: 404 Not Found
Body:
{
    "error": {
        "message": "Tweet is not found with tweet id = 220”
    }
}
```

### If we try to find another User’s Tweet details by Tweet Id in path url

* Request
```
GET /users/{1}/tweets/{22}
Host: localhost:3000
```
* Response
```
Status code: 403 Forbidden
Body:
{
    "error": {
        "message": "Author cannot see another author's complete tweet details!"
    }
}
```

## Follow APIs

### Follow a User

* Request
```
POST /users/{1}/follow
Host: localhost:3000
Content-Type: application/json
{
    "followsId": 2
}
```
* Response
```
Status code: 200 OK
Body:
{
    "data": {
        "followerId": 1,
        "followsId": 2
    }
}
```

### If we try to provide same User Id as follower Id and follows Id to follow

* Request
```
POST /users/{1}/follow
Host: localhost:3000
Content-Type: application/json
{
    "followsId": 1
}
```
* Response
```
Status code: 400 Bad Request
Body:
{
    "error": {
        "message": "User id = 1 cannot follow itself!"
    }
}
```

### If we try to provide not existed User Id in the path url as follower Id

* Request
```
POST /users/{1}/follow
Host: localhost:3000
Content-Type: application/json
{
    "followsId": 2
}
```
* Response
```
Status code: 404 Not Found
Body:
{
    "error": {
        "message": "User is not found with follower Id = 1"
    }
}
```

### If we try to provide not existed User Id in the request body as follows Id

* Request
```
POST /users/{1}/follow
Host: localhost:3000
Content-Type: application/json
{
    "followsId": 2
}
```
* Response
```
Status code: 404 Not Found
Body:
{
    "error": {
        "message": "User is not found with follows Id = 2”
    }
}
```

### If we try to provide User Id as follows Id who is already following

* Request
```
POST /users/{1}/follow
Host: localhost:3000
Content-Type: application/json
{
    "followsId": 2
}
```
* Response
```
Status code: 400 Bad Request
Body:
{
    "error": {
        "message": "User Id = 1 is already following User Id = 2”
    }
}
```

### Get Followers of a User by User Id

* Request
```
GET /users/{1}/followers
Host: localhost:3000
```
* Response
```
Status code: 200 OK
Body:
{
    "data": {
        "count": 2,
        "followers": [
            {
                "name": "Thanos",
                "id": 4
            },
            {
                "name": “Thor”,
                "id": 2
            }
        ]
    }
}
```

### If we try to provide not existed User Id in the path url

* Request
```
GET /users/{1}/followers
Host: localhost:3000
```
* Response
```
Status code: 404 Not Found
Body:
{
    "error": {
        "message": "User not found with id = 1”
    }
}
```

### Get Follows of a User by User Id

* Request
```
GET /users/{1}/follows
Host: localhost:3000
```
* Response
```
Status code: 200 OK
Body:
{
    "data": {
        "count": 4,
        "follows": [
            {
                "name": "Spider Man",
                "id": 5
            },
            {
                "name": "Captain",
                "id": 3
            },
            {
                "name": "Thor",
                "id": 2
            },
            {
                "name": "Thanos",
                "id": 4
            }
        ]
    }
}
```

### If we try to provide not existed User Id in the path url

* Request
```
GET /users/{1}/follows
Host: localhost:3000
```
* Response
```
Status code: 404 Not Found
Body:
{
    "error": {
        "message": "User not found with id = 1”
    }
}
```

### Unfollow a User

* Request
```
POST /users/{1}/unfollow
Host: localhost:3000
Content-Type: application/json
{
    "followsId": 2
}
```
* Response
```
Status code: 200 OK
Body:
{
    "data": {
        "message": "User Id = 1 unfollowed User Id = 2"
    }
}
```

### If we try to provide same User Id as follower Id and follows Id to unfollow

* Request
```
POST /users/{1}/unfollow
Host: localhost:3000
Content-Type: application/json
{
    "followsId": 1
}
```
* Response
```
Status code: 400 Bad Request
Body:
{
    "error": {
        "message": "User id = 1 cannot unfollow itself!"
    }
}
```

### If we try to provide User Id as follows Id who is not following

* Request
```
POST /users/{1}/unfollow
Host: localhost:3000
Content-Type: application/json
{
    "followsId": 2
}
```
* Response
```
Status code: 400 Bad Request
Body:
{
    "error": {
        "message": "User Id = 1 is not following User Id = 2”
    }
}
```
