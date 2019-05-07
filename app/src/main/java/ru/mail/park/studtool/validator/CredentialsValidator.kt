package ru.mail.park.studtool.validator

class CredentialsValidator {
    fun ValidateEmail(email: String): Pair<Boolean, String?> {
        if (email.isEmpty()) {
            return Pair(false, "Email is required!")
        }
        if (!email.contains('@')) {
            return Pair(false, "Invalid email!")
        }
        return Pair(true, null)
    }

    fun ValidatePassword(password: String): Pair<Boolean, String?> {
        if (password.isEmpty()) {
            return Pair(false, "Password is empty!")
        }
        if (password.length < 8) {
            return Pair(false, "Password too short!")
        }
        return Pair(true, null)
    }
}
