<template id="main-page">
    <div>
        <h1 class="greeting-header">Welcome to Specimen Base!</h1>
        <h2>Your email is {{user_email}}</h2>
    </div>
</template>
<script>
    Vue.component("main-page", {
        template: "#main-page",
        data: () => ({
            user_email: ""
        }),
        created() {
            fetch("/user-details")
                .then(res => res.json())
                .then(res => this.user_email = res)
                .catch(() => alert("Error while fetching user email"));
        }
    });
</script>

<style>
    .greeting-header {
        color: goldenrod;
    }
</style>