/*!
 * FlopStyles
 * (c) 2021 lincong1987
 */

let sizes = {
	s: {
		width: '24px',
		height: '32px',
		lineHeight: '32px',
		fontSize: '24px',
		fontWeight: 'bold',
		fontFamily: 'DINPro-Bold, DINPro',
		borderRadius: '2px',
	},
	m: {
		width: '26px',
		height: '36px',
		lineHeight: '36px',
		fontSize: '30px',
		fontWeight: 'bold',
		fontFamily: 'DINPro-Bold, DINPro',
		borderRadius: '3px',
	},
	l: {
		width: '32px',
		height: '44px',
		lineHeight: '44px',
		fontSize: '32px',
		fontWeight: 'bold',
		fontFamily: 'DINPro-Bold, DINPro',
		borderRadius: '3px',
	},
	xl: {
		width: '44px',
		height: '60px',
		lineHeight: '60px',
		fontSize: '44px',
		fontWeight: 'bold',
		fontFamily: 'DINPro-Bold, DINPro',
		borderRadius: '4px',
	},
}

let backgrounds = {
	red: {
		background: 'linear-gradient(rgba(173, 45, 45, 1), rgba(216, 74, 86, 1))',
	},
	tomato: {
		background: 'linear-gradient(rgba(163, 35, 0, 1), rgba(251, 118, 70, 1))',
	},
	orange: {
		background: 'linear-gradient(rgba(201, 144, 74, 1), rgba(226, 195, 146, 1))',
	},
	yellow: {
		color: 'rgba(6, 45, 139, 1)',
		background: 'linear-gradient(rgba(249, 253, 38, 1), rgba(253, 255, 128, 1))',
	},
	green: {
		background: 'linear-gradient(rgba(70, 153, 97, 1), rgba(148, 193, 144, 1))',
	},
	cyan: {
		background: 'linear-gradient(rgba(27, 160, 155, 1), rgba(125, 228, 220, 1))',
	},
	blue: {
		background: 'linear-gradient(rgba(59, 81, 255, 1), rgba(88, 138, 255, 1))',
	},
	purple: {
		background: 'linear-gradient(rgba(125, 47, 209, 1), rgba(163, 113, 234, 1))',
	},
	magenta: {
		background: 'linear-gradient(rgba(237, 19, 124, 1), rgba(249, 98, 181, 1))',
	},
	grey: {
		background: 'linear-gradient(rgba(142, 142, 142, 1), rgba(226, 226, 226, 1))',
	},

}

const flopThemes = {
	...sizes, ...backgrounds,

	...{
		red_m: {},
		red_l: {},
		red_xl: {},
		tomato_s: {},
		tomato_m: {},
		tomato_l: {},
		tomato_xl: {},
		orange_s: {},
		orange_m: {},
		orange_l: {},
		orange_xl: {},
		yellow_s: {},
		yellow_m: {},
		yellow_l: {},
		yellow_xl: {},
		green_s: {},
		green_m: {},
		green_l: {},
		green_xl: {},
		cyan_s: {},
		cyan_m: {},
		cyan_l: {},
		cyan_xl: {},
		blue_s: {},
		blue_m: {},
		blue_l: {},
		blue_xl: {},
		purple_s: {},
		purple_m: {},
		purple_l: {},
		purple_xl: {},
		magenta_s: {},
		magenta_m: {},
		magenta_l: {},
		magenta_xl: {},
		grey_s: {},
		grey_m: {},
		grey_l: {},
		grey_xl: {},
	},
}

for (const size in sizes) {
	for (const background in backgrounds) {
		flopThemes[`${background}_${size}`] =
			{...sizes[size], ...backgrounds[background]}

//		flopThemes[size][background] =
//			{...sizes[size], ...backgrounds[background]}
	}
}

export default {

	data () {
		return {
			flopThemes,
		}
	},

}
