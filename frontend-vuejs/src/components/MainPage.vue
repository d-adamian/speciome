<template>
    <div>
        <NavBar></NavBar>
        <SamplesTable></SamplesTable>
    </div>
</template>
<script>
import NavBar from '../components/NavBar.vue'
import SamplesTable from '../components/SamplesTable.vue'

export default {
    name: 'MainPage',
    components: {
        NavBar, SamplesTable
    },
    data: () => []
}
</script>

<style>
</style>