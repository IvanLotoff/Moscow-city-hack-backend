package ivan.projects.hakatonbackend.domain

data class User(var id : String,
                var username : String,
                var password : String,
                var authority : String)
