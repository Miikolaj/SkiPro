<script lang="ts">
	import { Button } from '$lib/components';
	import { faArrowRightToBracket } from '@fortawesome/free-solid-svg-icons';
</script>

<div class="navbar-contents">
	<div class="name">
		SKIPRO
	</div>
	<div class="buttons">
		<Button type="action" suffixIcon={faArrowRightToBracket}>Sign in</Button>
	</div>
</div>

	<style lang="scss">
    .navbar-contents {
      display: flex;
      justify-content: flex-end;
      align-items: center;
      width: 100%;
      padding: 15px 50px;
      position: relative;
    }

    .name {
      position: absolute;
      left: 50%;
      transform: translateX(-50%);
      font-family: $font-logo;
      font-size: 3rem;
    }

    .buttons {
      display: inline-flex;
      gap: 15px;
      font-size: 1.125rem;
      font-weight: 300;
      word-wrap: break-word;
    }
	</style>