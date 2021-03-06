from googletrans import Translator 

def translate(text, from_lang, to_lang):
	query = text
	if 'what' in query or 'who' in query or 'where' in query or 'how' in query or 'when' in query or 'which' in query or 'will' in query or 'whom' in query or 'whoose' in query or 'why' in query or 'whether' in query or 'did you' in query:
		query = query + "?"
	query = query.replace("translate", "")
	translator = Translator()
	translated_text = translator.translate(query, dest=to_lang)
	print(translated_text.pronunciation)
	return translated_text.text

if __name__ == '__main__':
	translated_text = translate("okay", "en", "de")
	print(translated_text)
