[BITS 32]

push ebp
mov ebp, esp
push edx
push ecx
cmp ecx, edx
jz  label_error
mov eax, 041424344h
mov edx, dword [ ebp + 08h]
mov ecx, dword [ ebp + 0ch]
add ecx, edx
mov eax, ecx
label_error:
pop edx
pop ecx
pop ebp
retn 08h
