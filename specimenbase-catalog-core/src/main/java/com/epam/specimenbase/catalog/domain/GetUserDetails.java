package com.epam.specimenbase.catalog.domain;

public final class GetUserDetails {
    public static final class Response {
        private final String email;

        public Response(String email) {
            this.email = email;
        }

        public String getEmail() {
            return email;
        }
    }

    public Response execute() {
        String userEmail = "user@company.com";
        return new Response(userEmail);
    }
}
