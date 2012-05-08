package util.cipher


trait LogCipher {
	def crunchArray(input: Array[Byte]):Array[Byte]
}