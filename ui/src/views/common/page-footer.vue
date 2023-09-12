<template>
    <div
        class="pdt10 pdb10"
        style="width: 95%; height: 100%; margin: 0 auto 0 auto"
    >
        <slot></slot>
    </div>
</template>
<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
@Component({})
export default class PageFooterComponent extends Vue {}
</script>
