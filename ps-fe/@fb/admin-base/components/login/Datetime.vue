<template>

    <div class="jpx-datetime">
        {{ date }}
    </div>

</template>

<script>
    /**
     * header
     * (c) 2021 lincong1987
     */

    export default {
        props: {
            format: {
                type: [String],
                default: 'YYYY年MM月DD日 dddd HH:mm:ss'
            }
        },
        data () {

            return {
                date: '',
            }
        },
        computed: {},
        mounted () {
            this.date = this.$dayjs().format(this.format)
            setInterval(() => {
                this.date = this.$dayjs().format(this.format)
            }, 1000)
        },
    }
</script>