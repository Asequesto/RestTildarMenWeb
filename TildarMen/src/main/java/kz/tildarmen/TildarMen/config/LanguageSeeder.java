package kz.tildarmen.TildarMen.config;

import kz.tildarmen.TildarMen.model.Language;
import kz.tildarmen.TildarMen.repository.LanguageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LanguageSeeder implements CommandLineRunner {

    private final LanguageRepository languageRepository;

    @Override
    public void run(String... args) {
        if (languageRepository.count() == 0) { // Check if the database is empty
            List<Language> languages = List.of(
                    new Language(null, "Afrikaans", "af"),
                    new Language(null, "Albanian", "sq"),
                    new Language(null, "Amharic", "am"),
                    new Language(null, "Arabic", "ar"),
                    new Language(null, "Armenian", "hy"),
                    new Language(null, "Azerbaijani", "az"),
                    new Language(null, "Basque", "eu"),
                    new Language(null, "Belarusian", "be"),
                    new Language(null, "Bengali", "bn"),
                    new Language(null, "Bosnian", "bs"),
                    new Language(null, "Bulgarian", "bg"),
                    new Language(null, "Catalan", "ca"),
                    new Language(null, "Cebuano", "ceb"),
                    new Language(null, "Chinese", "zh"),
                    new Language(null, "Corsican", "co"),
                    new Language(null, "Croatian", "hr"),
                    new Language(null, "Czech", "cs"),
                    new Language(null, "Danish", "da"),
                    new Language(null, "Dutch", "nl"),
                    new Language(null, "English", "en"),
                    new Language(null, "Esperanto", "eo"),
                    new Language(null, "Estonian", "et"),
                    new Language(null, "Finnish", "fi"),
                    new Language(null, "French", "fr"),
                    new Language(null, "Galician", "gl"),
                    new Language(null, "Georgian", "ka"),
                    new Language(null, "German", "de"),
                    new Language(null, "Greek", "el"),
                    new Language(null, "Gujarati", "gu"),
                    new Language(null, "Haitian Creole", "ht"),
                    new Language(null, "Hausa", "ha"),
                    new Language(null, "Hebrew", "he"),
                    new Language(null, "Hindi", "hi"),
                    new Language(null, "Hungarian", "hu"),
                    new Language(null, "Icelandic", "is"),
                    new Language(null, "Igbo", "ig"),
                    new Language(null, "Indonesian", "id"),
                    new Language(null, "Irish", "ga"),
                    new Language(null, "Italian", "it"),
                    new Language(null, "Japanese", "ja"),
                    new Language(null, "Javanese", "jv"),
                    new Language(null, "Kannada", "kn"),
                    new Language(null, "Kazakh", "kk"),
                    new Language(null, "Khmer", "km"),
                    new Language(null, "Korean", "ko"),
                    new Language(null, "Kurdish", "ku"),
                    new Language(null, "Kyrgyz", "ky"),
                    new Language(null, "Lao", "lo"),
                    new Language(null, "Latin", "la"),
                    new Language(null, "Latvian", "lv"),
                    new Language(null, "Lithuanian", "lt"),
                    new Language(null, "Luxembourgish", "lb"),
                    new Language(null, "Macedonian", "mk"),
                    new Language(null, "Malagasy", "mg"),
                    new Language(null, "Malay", "ms"),
                    new Language(null, "Malayalam", "ml"),
                    new Language(null, "Maltese", "mt"),
                    new Language(null, "Maori", "mi"),
                    new Language(null, "Marathi", "mr"),
                    new Language(null, "Mongolian", "mn"),
                    new Language(null, "Myanmar", "my"),
                    new Language(null, "Nepali", "ne"),
                    new Language(null, "Norwegian", "no"),
                    new Language(null, "Odia (Oriya)", "or"),
                    new Language(null, "Pashto", "ps"),
                    new Language(null, "Persian", "fa"),
                    new Language(null, "Polish", "pl"),
                    new Language(null, "Portuguese", "pt"),
                    new Language(null, "Punjabi", "pa"),
                    new Language(null, "Romanian", "ro"),
                    new Language(null, "Russian", "ru"),
                    new Language(null, "Samoan", "sm"),
                    new Language(null, "Scottish Gaelic", "gd"),
                    new Language(null, "Serbian", "sr"),
                    new Language(null, "Sesotho", "st"),
                    new Language(null, "Shona", "sn"),
                    new Language(null, "Sindhi", "sd"),
                    new Language(null, "Sinhala", "si"),
                    new Language(null, "Slovak", "sk"),
                    new Language(null, "Slovenian", "sl"),
                    new Language(null, "Somali", "so"),
                    new Language(null, "Spanish", "es"),
                    new Language(null, "Sundanese", "su"),
                    new Language(null, "Swahili", "sw"),
                    new Language(null, "Swedish", "sv"),
                    new Language(null, "Tagalog", "tl"),
                    new Language(null, "Tajik", "tg"),
                    new Language(null, "Tamil", "ta"),
                    new Language(null, "Tatar", "tt"),
                    new Language(null, "Telugu", "te"),
                    new Language(null, "Thai", "th"),
                    new Language(null, "Turkish", "tr"),
                    new Language(null, "Ukrainian", "uk"),
                    new Language(null, "Urdu", "ur"),
                    new Language(null, "Uzbek", "uz"),
                    new Language(null, "Vietnamese", "vi"),
                    new Language(null, "Welsh", "cy"),
                    new Language(null, "Xhosa", "xh"),
                    new Language(null, "Yiddish", "yi"),
                    new Language(null, "Yoruba", "yo"),
                    new Language(null, "Zulu", "zu")
            );
            languageRepository.saveAll(languages);
        }
    }
}
