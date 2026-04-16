import type { InputHTMLAttributes, TextareaHTMLAttributes } from "react";

type BaseProps = {
  label: string;
  error?: string;
};

type InputProps = BaseProps & {
  textarea?: false;
} & InputHTMLAttributes<HTMLInputElement>;

type TextareaProps = BaseProps & {
  textarea: true;
} & TextareaHTMLAttributes<HTMLTextAreaElement>;

type FormInputProps = InputProps | TextareaProps;

export function FormInput(props: FormInputProps) {
  const { label, error } = props;
  const inputClass =
    "w-full rounded-lg border border-slate-300 bg-white px-3 py-2 text-sm outline-none transition focus:border-blue-500 focus:ring-2 focus:ring-blue-100";

  return (
    <label className="block space-y-1 text-sm text-slate-700">
      <span className="font-medium">{label}</span>
      {props.textarea ? (
        <textarea {...props} className={inputClass} rows={4} />
      ) : (
        <input {...props} className={inputClass} />
      )}
      {error ? <span className="text-xs text-red-600">{error}</span> : null}
    </label>
  );
}
