import { useEffect } from "react";
import { useTranslation } from "react-i18next";

/** Keeps <html lang="…"> in sync with the active i18n locale. */
export function I18nHtmlLang() {
  const { i18n } = useTranslation();

  useEffect(() => {
    const lang = i18n.resolvedLanguage ?? i18n.language;
    document.documentElement.lang = lang.split("-")[0] ?? "en";
  }, [i18n.language, i18n.resolvedLanguage]);

  return null;
}
