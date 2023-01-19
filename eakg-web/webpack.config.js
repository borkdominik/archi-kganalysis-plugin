const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const MiniCssExtractPlugin = require("mini-css-extract-plugin");

module.exports = {
    devtool: 'inline-source-map',
	entry: './src/index.ts',
	output: {
		filename: 'index.js',
		path: path.resolve(__dirname, 'dist')
	},
    plugins: [
        new HtmlWebpackPlugin({
            template: 'src/assets/index.html'
        }),
        new MiniCssExtractPlugin()
    ],
    module: {
        rules: [
            {
                test: /\.([cm]?ts|tsx)$/,
                loader: 'ts-loader',
            },
            {
                test: /\.css$/i,
                use: [MiniCssExtractPlugin.loader, "css-loader"]
            },
        ],
    },
    resolve: {
        extensions: [ '.ts', '.tsx', '.js' ],
        extensionAlias: {
            '.ts': ['.js', '.ts'],
            '.cts': ['.cjs', '.cts'],
            '.mts': ['.mjs', '.mts']
        }
    }
};